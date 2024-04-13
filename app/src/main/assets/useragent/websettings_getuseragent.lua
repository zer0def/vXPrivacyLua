function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local setting = param:getSetting("user.agent")
    if setting == nil then
        return false
    end

    param:setResult(setting)
    return true, res, setting
end