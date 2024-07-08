import { NextAuthOptions } from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";
import { z } from "zod";
import axios from "axios";
import { refreshAccessToken } from "@/utils/refreshAccessToken";

// There are 1000 millis in 1 second, 60 seconds in 1 min, 60 minutes in 1 hour
const refreshTokenBeforeExpiryTime = 60 * 60 * 1000;
const API_URL = process.env.NEXT_PUBLIC_API_URL;
const AUTH_SECRET = process.env.NEXTAUTH_SECRET;

export const authOptions: NextAuthOptions = {
  providers: [
    CredentialsProvider({
      name: "Credentials",
      credentials: {
        email: { label: "email", type: "text" },
        password: { label: "password", type: "password" },
        browserName: { type: "text" },
        deviceType: { type: "text" }
      },
      async authorize(credentials) {
        const parsedCredentials = z
          .object({
            email: z.string().email().min(5).max(320),
            password: z.string().min(5).max(50),
            browserName: z.string(),
            deviceType: z.string()
          })
          .safeParse(credentials);

        if (parsedCredentials.success) {
          const { email, password, browserName, deviceType } = parsedCredentials.data;

          try {
            const deviceInfo = {
              browserName: browserName,
              deviceType: deviceType
            };
            const user = await axios.post(API_URL + "/auth/login", {
              email: email,
              password: password,
              deviceInfo: deviceInfo
            });
            if (user.data.accessToken) {
              return user.data;
            }
          } catch (e) {
            console.log(e);
            throw new Error(e);
          }
        }
        return null;
      }
    })
  ],

  callbacks: {
    async jwt({ token, user }) {
      if (user) {
        // Executed only at login
        token.userId = user.userId;
        token.accessToken = user.accessToken;
        token.accessTokenExpiry = user.accessTokenExpiry;
        token.refreshToken = user.refreshToken;
      }

      // accessTokenExpiry is 24 hours, token has to be refreshed before 24 hours
      const shouldRefreshTime = Math.round(token.accessTokenExpiry - refreshTokenBeforeExpiryTime
        - Date.now());

      // Return token if it's valid
      if (shouldRefreshTime > 0) {
        return Promise.resolve(token);
      }

      // Refresh token after 23 hours
      token = await refreshAccessToken(token);
      return Promise.resolve(token);
    },

    async session({ session, token }) {
      // Pass accessToken to client
      session.userId = token.userId;
      session.accessToken = token.accessToken;
      session.accessTokenExpiry = token.accessTokenExpiry;
      session.error = token.error;

      return Promise.resolve(session);
    }
  },

  pages: {
    signIn: "/signin"
  },

  secret: AUTH_SECRET
};