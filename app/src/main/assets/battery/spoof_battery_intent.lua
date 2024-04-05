function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local ths = param:getThis()
    if ths == nil then
        return false
    end

    local intent = ths:getAction()
    if intent == nil then
        return false
    end

    local param = param:getArgument(0)
    if param == nil then
        return false
    end

    local bmn = luajava.bindClass("android.os.BatteryManager")
    if bmn == nil then
        log("ERROR BatteryManager is NULL")
        return false
    end
    log("Intent: " .. intent .. " arg=" .. param)

    if intent == "android.intent.action.BATTERY_CHANGED" or intent == "BATTERY_CHANGED" then
        log("Filtering [BATTERY_CHANGED] Intent")
        if param == "scale" then
            param:setResult(100)
            return true, tostring(res), "100"
        end

        if param == "level" then

            --tofinish
        end
    end

end