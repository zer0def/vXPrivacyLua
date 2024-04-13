function after(hook, param)
    log("New AD ID Hook Invoked  (adid)")
    local res = param:filterBinder("adid")
    if res == nil then
        log("New AD ID target is NIL  (adid)")
        return false
    end

    log("Filtered the AD ID  (adid)")
    param:setResult(true)
    return true
end