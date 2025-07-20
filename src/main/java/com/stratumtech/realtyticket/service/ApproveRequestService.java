package com.stratumtech.realtyticket.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ApproveRequestService {

    List<Map<String, Object>> getRequests();

    List<Map<String, Object>> getRejectedRequests();

    boolean approveRequest(UUID requestId, UUID approverUuid);

    boolean rejectRequest(UUID requestId, UUID rejecterUuid);

}
