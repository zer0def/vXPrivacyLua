function after(hook, param)
    local ths = param:getThis()
    if ths == nil then
        return false
    end

    local ctx = param:getApplicationContext()
    if ctx == nil then
        return false
    end

    if param:hasFunction("gdprForgetMe") == true then
        ths:gdprForgetMe(ctx)
    end

    if param:hasFunction("disableThirdPartySharing")  == true then
        ths:disableThirdPartySharing(ctx)
    end

    return true
end