if (object == null) {
	return null;
}

if (object instanceof Integer) {
    return (Integer) object;
}

return Integer.valueOf(object.toString());