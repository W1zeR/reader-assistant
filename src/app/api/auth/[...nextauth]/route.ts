import axios from "axios";
import NextAuth from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";
import { RefreshToken } from "@/types/quote";

const API_URL = process.env.API_URL
// There are 1000 mils in 1 second, 60 seconds in 1 min, 60 minutes in 1 hour
const refreshTokenBeforeExpiryTime = 60 * 60 * 1000

async function refreshAccessToken(tokenObject: RefreshToken) {
  try {
    const tokenResponse = await axios.post(API_URL + "auth/refresh", {
      token: tokenObject
    })

    return {
      refreshToken: tokenResponse.data.refreshToken,
      accessToken: tokenResponse.data.accessToken,
      accessTokenExpiry: tokenResponse.data.accessTokenExpiry
    }
  } catch (error) {
    return {
      error: "RefreshAccessTokenError"
    }
  }
}

const providers = [
  CredentialsProvider({
    name: "Credentials",
    credentials: {
      email: { label: "Email", type: "text" },
      password: { label: "Password", type: "password" }
    },
    async authorize(credentials) {
      try {
        const user = await axios.post(API_URL + "auth/login", {
          email: credentials.email,
          password: credentials.password
        });
        if (user.data.accessToken) {
          return user.data
        }
        return null;
      } catch (e) {
        throw new Error(e)
      }
    }
  })
];

const callbacks = {
  async jwt({ token, user }) {
    if (user) {
      // This will only be executed at login. Each next invocation will skip this part.
      token.accessToken = user.data.accessToken
      token.accessTokenExpiry = user.data.accessTokenExpiry
      token.refreshToken = user.data.refreshToken
    }

    // If accessTokenExpiry is in 24 hours, we have to refresh token before 24 hours pass.
    const shouldRefreshTime = Math.round((token.accessTokenExpiry - refreshTokenBeforeExpiryTime)
      - Date.now())

    // If the token is still valid, just return it.
    if (shouldRefreshTime > 0) {
      return token
    }

    // If the call arrives after 23 hours have passed, we allow to refresh the token.
    token = refreshAccessToken(token)
    return token
  },

  async session({ session, token }) {
    // Here we pass accessToken to the client to be used in authentication with your API
    session.accessToken = token.accessToken;
    session.accessTokenExpiry = token.accessTokenExpiry;
    session.error = token.error;

    return session;
  }
};

const handler = NextAuth({
  providers: providers,
  callbacks: callbacks,
  pages: {
    signIn: '/signin',
    signOut: '/signout'
  },
  secret: 'UevGuOKrzTkpH8fPfHth1Z6pTlXr18JrDaw3H/nqtA0='
});

export { handler as GET, handler as POST };
