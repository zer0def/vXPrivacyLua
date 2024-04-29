function before(hook, param)
    local ths = param:getThis()
    if ths == nil then
        return false
    end

    local path = ths:getAbsolutePath()
    if path == nil then
        return false
    end

    if param:fileIsEvidence(path, 3) then
        log("File.exists() IS Emulator Root, Returning False... [" .. path .. "]")
        param:setResult(false)
        return true
    end

    return false
end