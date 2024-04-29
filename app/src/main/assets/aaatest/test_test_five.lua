function before(hook, param)
    local filter = param:filterBinderProxyBefore("adid")
    if filter == nil then
        return false
    end

    log("Filtered AD ID: new ID=" .. filter)
    param:setResult(true)
    return true, "old", filter
end