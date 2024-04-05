function before(hook, param)
    local line = 'File: '
    local arg = param:getArgument(0)
    if arg == nil then
        return false
    end


    line = line .. arg
    local argTwo = param:getArgument(1)
    if argTwo ~= nil then
        line = line .. ' flags=' .. argTwo
    end

    log('[xlog] File being Opened Via Low Level Core IO [open], ' .. line)
    return false
end