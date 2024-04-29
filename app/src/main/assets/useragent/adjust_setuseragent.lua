function before(hook, param)
    local setting = param:getSetting("user.agent")
    local old = param:getArgument(0)
    if setting == nil or old == nil then
        return false
    end

    param:setArgument(0, setting)
    return true, old, setting
end