package consumindowebservice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExpenseSheet {
	private LocalDate dateToday = LocalDate.now();
	private final String id = "'/ExpenseSheet'";
	private String ExternalId;
	private final String Description = "'Nova Planilha de Despesas criada por meio da API Clarizen'";
	private final String date = "'" + dateToday.format(DateTimeFormatter.ofPattern("yyyy")) + "-"
			+ dateToday.format(DateTimeFormatter.ofPattern("MM")) + "-"
			+ dateToday.format(DateTimeFormatter.ofPattern("dd")) + "'";

	public final String getId() {
		return id;
	}

	public String getExternalId() {
		return ExternalId;
	}

	public void setExternalId(String externalId) {
		ExternalId = externalId;
	}

	public final String getDescription() {
		return Description;
	}

	public final String getDate() {
		return date;
	}

}
