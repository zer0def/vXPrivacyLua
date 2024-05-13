function before(hook, param)
    local setting = param:getSetting("account.user.id")
    if setting == nil then
        return false
    end
    param:setResult(setting)
    return true
end