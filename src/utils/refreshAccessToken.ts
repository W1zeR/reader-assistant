import { JWT } from "next-auth/jwt";
import axios from "axios";

const API_URL = process.env.NEXT_PUBLIC_API_URL;

export async function refreshAccessToken(tokenObject: JWT) {
  try {
    // Get a new set of tokens with a refreshToken
    const tokenResponse: JWT = await axios.post(API_URL + "/auth/refresh", {
      token: tokenObject.refreshToken
    });

    return {
      userId: tokenResponse.userId,
      accessToken: tokenResponse.accessToken,
      refreshToken: tokenResponse.refreshToken,
      accessTokenExpiry: tokenResponse.accessTokenExpiry,
      error: null
    };
  } catch (error) {
    return {
      userId: null,
      accessToken: null,
      refreshToken: null,
      accessTokenExpiry: null,
      error: "RefreshAccessTokenError"
    };
  }
}
