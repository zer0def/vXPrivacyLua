function before(hook, param)
    local arg = param:getArgument(0)
    if arg == nil then
        return false
    end

    log('[xlog] File being Constructed File(Uri) => ' .. arg:getPath())
    return false
end