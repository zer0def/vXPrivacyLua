function before(hook, param)
    local ths = param:getThis()
    if ths == nil then
        return false
    end

    local type = ths:getType()
    if type == 0x11 then
        log("Spoofing VPN State NetworkInfo 0x11")
        param:setResult(false)
        return true
    end

    return false
end