--function before(hook, param)
    --local filter = param:filterBinderProxyBefore("adid")
    --if filter == nil then
    --    return false
    --end
    --log("Filtered AD ID: new ID=" .. filter)
    --param:setResult(true)
    --return true, "N/A", filter
--end
function after(hook, param)
    local filter = param:filterBinderProxyAfter("adid")
    if filter == nil then
        return false
    end

    log("Filtered AD ID: new ID=" .. filter)
    param:setResult(true)
    return true, "N/A", filter
end