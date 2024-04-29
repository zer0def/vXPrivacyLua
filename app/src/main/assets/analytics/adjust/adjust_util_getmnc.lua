function before(hook, param)
    local setting = param:getSetting("gsm.operator.mnc")
    if setting == nil then
        return false
    end

    param:setResult(setting)
    return true
end