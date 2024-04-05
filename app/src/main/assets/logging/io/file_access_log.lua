function before(hook, param)
    local line = 'File: '
    local argOne = param:getArgument(0)
    if arg == nil then
        return false
    end

    line = line .. argOne
    local argTwo = param:getArgument(1)
    if argTwo ~= nil then
        line = line .. ' mode=' .. argTwo
    end

    log('[xlog] (access) ' .. line)
    return false
end