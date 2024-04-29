function after(hook, param)
    local ths = param:getThis()
    if ths == nil then
        return false
    end

    local ctx = param:getApplicationContext()
    if ctx == nil then
        return false
    end

    local clss = "com.adjust.sdk.AdjustInstance"
    --Lets just use the this object pass it
    ths:gdprForgetMe(ctx)
    ths:disableThirdPartySharing(ctx)
    --if param:hasFunction(clss, "gdprForgetMe") == true then
    ---    ths:gdprForgetMe(ctx)
    --end

    --if param:hasFunction(clss, "disableThirdPartySharing")  == true then
    --    ths:disableThirdPartySharing(ctx)
    --end

    return true
end