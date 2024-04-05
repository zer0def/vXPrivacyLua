function before(hook, param)
    local line = 'File: '
    local argOne = param:getArgument(0)
    if arg == nil then
        return false
    end

    line = line .. argOne
    local argTwo = param:getArgument(1)
    if argTwo ~= nil then
        line = line .. ' uid=' .. argTwo
    end

    local argThree = param:getArgument(2)
    if argThree ~= nil then
        line = line .. ' gid=' .. argThree
    end

    log('[xlog] (chown) ' .. line)
    return false
end