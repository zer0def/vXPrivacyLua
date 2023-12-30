function before(hook, param)
    local ths = param:getThis()
    if ths == nil then
        return false
    end

    if param:isDriverFile(ths:getPath()) then
        log("File.exists() IS Driver, Returning False... [" .. arg1 .. "]")
        param:setResult(false)
        return true
    end

    return false
end