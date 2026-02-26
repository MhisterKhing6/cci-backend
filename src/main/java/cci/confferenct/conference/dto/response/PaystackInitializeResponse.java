package cci.confferenct.conference.dto.response;

public record PaystackInitializeResponse(
        boolean status,
        String message,
        Data data
) {
    public record Data(
            String authorization_url,
            String access_code,
            String reference
    ) {}
}