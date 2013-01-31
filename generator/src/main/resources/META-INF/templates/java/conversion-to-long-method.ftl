if (object == null) {
	return null;
}

if (object instanceof Long) {
    return (Long) object;
}

return Long.valueOf(object.toString());