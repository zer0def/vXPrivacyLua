function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local fake = param:getSetting("network.host.address", "127.0.0.1")
    param:setResult(fake)
    return true, res, fake
end
