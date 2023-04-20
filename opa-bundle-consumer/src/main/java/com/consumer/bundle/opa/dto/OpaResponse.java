package com.consumer.bundle.opa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpaResponse {

  public Map<String, Boolean> result;
}
