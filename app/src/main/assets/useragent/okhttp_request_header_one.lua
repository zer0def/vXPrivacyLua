function before(hook, param)
    local p1 = param:getArgument(0)
    local p2 = param:getArgument(1)

    if p1 == nil or p2 == nil then
        return false
    end

    local low = string.lower(p1)
    if low == nil then
        return false
    end

    if low ~= "user-agent" then
        return false
    end

    local setting = param:getSetting("user.agent")
    if setting == nil then
        return false
    end

    param:setArgumentString(1, setting)
    return true, p2, setting
end