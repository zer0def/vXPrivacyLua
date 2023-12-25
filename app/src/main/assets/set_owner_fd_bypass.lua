function before(hook, param)
    --Help with Hook V2 of CPU Intercept and any other

    local fd = param:getArgument(0)
    if fd == nil then
        return false
    end

    local ownerId = fd["getOwnerId$"](fd)
    if ownerId ~= nil and ownerId ~= 0  then
        log("Bypassing  [libcore.io.IoUtils.setFdOwner] Note this can cause odd behavior within the app so try to avoid using this Hook")
        param:setResult(null)
        return true
    end

    return false
end
