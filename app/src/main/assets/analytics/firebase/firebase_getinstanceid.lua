function after(hook, param)
    local ret = param:getResult()
    if ret == nil then
        return false
    end

    local setting = param:getSetting("analytics.firebase.instance.id")
    if setting == nil then
        return false
    end

    param:setResult(setting)
    return false, ret, setting
end