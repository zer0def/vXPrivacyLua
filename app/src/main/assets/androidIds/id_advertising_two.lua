function before(hook, param)
    log("New AD ID Hook Invoked  (ADID)")
    local res = param:filterBinder("adid")
    if res == nil then
        log("New AD ID target is NIL  (ADID)")
        return false
    end

    log("Filtered the AD ID  (ADID) NEW ID=" .. res)
    param:setResult(true)
    return true
end