package almeida.rafael.animeservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import almeida.rafael.animeservice.config.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/connections")
@Slf4j
@RequiredArgsConstructor
public class ConnectionController {
  private final Connection connectionMySql;

  @GetMapping
  public ResponseEntity<Connection> getConnections() {
    return ResponseEntity.ok(connectionMySql);
  }
}
