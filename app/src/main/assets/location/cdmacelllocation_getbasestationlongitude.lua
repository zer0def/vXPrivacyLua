function before(hook, param)
    local setting = param:getSetting("location.longitude")
    if setting == nil then
        return false
    end

    param:setResult(setting)
    return true
end