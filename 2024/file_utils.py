def to_string_list(file) -> [str]:
    return [l.strip() for l in open(file, "r").readlines()]
