function after(hook, param)
    local ths = param:getThis()
    local res = param:getResult()
    if res == nil then
        return false
    end

    if param:fileIsEvidence(ths:getAbsolutePath(), 3) then
        log("Is Root/Emulator Folder Root: " .. ths:getPath())
        param:setResult(null)
        return true
    end

    local fakes = param:stringArrayHasEvidence(res, 3)
    param:setResult(fakes)
    return true
end