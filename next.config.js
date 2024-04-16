/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: "https",
        pathname: "cdn.sanity.io",
        hostname: "localhost",
        port: "",
      },
    ],
  },
};

module.exports = nextConfig;
