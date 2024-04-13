function after(hook, param)
    local configuration = param:getResult()
    local mcc = param:getSettingInt("gsm.operator.mcc", 274)
    local mnc = param:getSettingInt("gsm.operator.mnc", 299)
    if mcc == nil or mnc == nil then
        return false
    end

    configuration.mcc = mcc
    configuration.mnc = mnc
    return true, "N/A", "mcc=" .. tostring(mcc) .. " mnc=" .. tostring(mnc)
end