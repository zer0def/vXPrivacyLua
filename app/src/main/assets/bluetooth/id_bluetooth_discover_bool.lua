function before(hook, param)
    local setting = param:getSetting("bluetooth.allow.discovery.bool")
    if setting == nil then
        return false
    end

    if setting == "true" then
        return false
    end

    param:setResult(false)
    return true, "N/A", setting
end