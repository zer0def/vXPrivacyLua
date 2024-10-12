function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local fake = param:getSetting("network.host.name", "Google.com")
    param:setResult(fake)
    return true, res, fake
end
