function after(hook, param)
    local ths = param:getThis()
    if ths == nil then
        return false
    end

    local setting = param:getSetting("user.agent")
    if setting == nil then
        return false
    end

    log("Settings Being Called! Mozilla Gecko: " .. setting)
    ths:setUserAgentOverride(setting)
    return true
end