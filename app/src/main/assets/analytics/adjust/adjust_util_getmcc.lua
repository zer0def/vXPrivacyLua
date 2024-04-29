function before(hook, param)
    local setting = param:getSetting("gsm.operator.mcc")
    if setting == nil then
        return false
    end

    param:setResult(setting)
    return true
end