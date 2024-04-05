function before(hook, param)
    local setting = param:getSetting("bluetooth.allow.discovery.bool")
    if setting == nil then
        return false
    end

    if setting == "true" then
        return false
    end

    log("Bluetooth NULL as flag dosnt want Discovery")
    param:setResult(null)
    return true
end