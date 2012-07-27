if (object == null) {
	return null;
}

if (object instanceof Float) {
    return (Float) object;
}

return Float.valueOf(object.toString());