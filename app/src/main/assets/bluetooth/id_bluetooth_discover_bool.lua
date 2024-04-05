function before(hook, param)
    local setting = param:getSetting("bluetooth.allow.discovery.bool")
    if setting == nil then
        return false
    end

    if setting == "true" then
        return false
    end

    log("Bluetooth Discovery Flag Flipped to not Discover")
    param:setResult(false)
    return true
end