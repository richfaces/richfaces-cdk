if (object == null) {
		    return true;
		} else if (object.getClass().isArray()) {
		    return ((Object[]) object).length == 0;
		} else if (object instanceof java.util.Collection) {
		    return ((java.util.Collection<?>) object).isEmpty();
		} else if (object instanceof java.util.Map) {
		    return ((java.util.Map<?, ?>) object).isEmpty();
		} else {
			return object.toString().length() == 0;
		}