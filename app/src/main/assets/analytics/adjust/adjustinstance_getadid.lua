function before(hook, param)
    local setting = param:getSettingReMap("unique.google.advertising.id", "ad.id")
    if setting == nil then
        return false
    end

    param:setResult(setting)
    return true
end