function before(hook, param)
    local setting = param:getSetting("unique.android.id")
    if setting == nil then
        return false
    end

    param:setResult(setting)
    return true
end