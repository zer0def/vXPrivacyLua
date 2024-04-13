function after(hook, param)
    local ths = param:getThis()
    if ths == nil then
        return false
    end

    local setting = param:getSetting("user.agent")
    if setting == nil then
        return false
    end

    log("Builder Being Called! Mozilla Gecko Fake: " .. setting)
    ths:userAgentOverride(setting)
    return true
end