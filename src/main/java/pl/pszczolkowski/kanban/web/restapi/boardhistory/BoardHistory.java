package pl.pszczolkowski.kanban.web.restapi.boardhistory;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BoardHistory {

	private final List<LocalDateTime> labels;
	private final List<String> series;
	private final List<List<Integer>> data;
	
	public BoardHistory(List<LocalDateTime> labels, List<String> series, List<List<Integer>> data) {
		this.labels = labels;
		this.series = series;
		this.data = data;
	}

	@ApiModelProperty("Labels for board history")
	public List<LocalDateTime> getLabels() {
		return labels;
	}

	@ApiModelProperty("Series for board history")
	public List<String> getSeries() {
		return series;
	}

	@ApiModelProperty("Board history data")
	public List<List<Integer>> getData() {
		return data;
	}
	
}
