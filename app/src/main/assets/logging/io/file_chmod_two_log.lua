function before(hook, param)
    local line = 'File ID: '
    local argOne = param:getArgument(0)
    if arg == nil then
        return false
    end

    local fId = param:getFileDescriptorId(argOne)
    local num = '0'
    if fId ~= nil then
        num = tostring(fId)
    end

    line = line .. num
    local argTwo = param:getArgument(1)
    if argTwo ~= nil then
        line = line .. ' mode=' .. argTwo
    end

    log('[xlog] (chmod) ' .. line)
    return false
end