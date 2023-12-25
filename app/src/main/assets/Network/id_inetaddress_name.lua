function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local fake = param:getSetting("net.host_name", "Google.com")
    param:setResult(fake)
    return true, res, fake
end
