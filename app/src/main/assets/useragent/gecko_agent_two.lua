function before(hook, param)
    local p = param:getArgument(0)
    if p == nil then
        return false
    end

    local setting = param:getSetting("user.agent")
    if setting == nil then
        return false
    end

    param:setArgumentString(0, setting)
    return true, p, setting
end