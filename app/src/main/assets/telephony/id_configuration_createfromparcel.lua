function after(hook, param)
    local configuration = param:getResult()
    configuration.mcc = param:getSettingInt("gsm.operator.mcc", 274)
    configuration.mnc = param:getSettingInt("gsm.operator.mnc", 299)
    return true
end