function after(hook, param)
    local intent = param:getResult()
    if intent == nil then
        return false
    end

    local action = intent:getAction()
    if action == nil then
        return false
    end

    local h = hook:getName()
    local match = string.gmatch(h, '[^/]+')
    local func = match()
    local name = match()

    if name == 'message' and action == 'android.provider.Telephony.SMS_RECEIVED' then
        local objectClass = luajava.bindClass('java.lang.Object')
        local arrayClass = luajava.bindClass('java.lang.reflect.Array')
        local objectArray = arrayClass:newInstance(objectClass, 0)
        intent:putExtra('pdus', objectArray)
        return true
    else
        return false
    end
end
