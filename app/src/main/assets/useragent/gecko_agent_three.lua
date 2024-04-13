function before(hook, param)
    local res = param:getArgument(0)
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