function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end
    local cleaned = param:stackHasEvidence(res)
    param:setResult(cleaned)
    return true
end