function after(hook, param)
    local ths = param:getThis()
    local res = param:getResult()
    if res == nil then
        return false
    end

    if param:fileIsEvidence(ths:getAbsolutePath(), 3) then
        log("Is Root/Emulator Folder Root: " .. ths:getAbsolutePath())
        param:setResult(null)
        return true
    end

    local fakes = param:fileArrayHasEvidence(res, 3)
    param:setResult(fakes)
    return true
end